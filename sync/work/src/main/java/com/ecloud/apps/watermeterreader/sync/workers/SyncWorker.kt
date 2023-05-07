package com.ecloud.apps.watermeterreader.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.ecloud.apps.watermeterreader.core.data.Synchronizer
import com.ecloud.apps.watermeterreader.core.data.repository.ProjectRepository
import com.ecloud.apps.watermeterreader.core.datastore.WmrPreferencesDataSource
import com.ecloud.apps.watermeterreader.core.network.Dispatcher
import com.ecloud.apps.watermeterreader.core.network.WmrDispatcher
import com.ecloud.apps.watermeterreader.sync.initializers.SyncConstraints
import com.ecloud.apps.watermeterreader.sync.initializers.syncForegroundInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val wmrPreferencesDataSource: WmrPreferencesDataSource,
    private val projectRepository: ProjectRepository,
    @Dispatcher(WmrDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        traceAsync("Sync", 0) {
            val userData = wmrPreferencesDataSource.userDataStream.first()
            val hasUrl = userData.selectedUrl.isNotEmpty()
            val hasBranchCode = userData.branchCode.isNotEmpty()
            val hasWarehouse = userData.projectCode.isNotEmpty()

            if (hasUrl && hasWarehouse && hasBranchCode) {
                val syncedSuccessfully = awaitAll(
                    async { projectRepository.sync() },
                    async { projectRepository.sync() }
                ).all { it }

                if (syncedSuccessfully) Result.success()
                else Result.retry()
            }

            Result.success()
        }
    }

    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
