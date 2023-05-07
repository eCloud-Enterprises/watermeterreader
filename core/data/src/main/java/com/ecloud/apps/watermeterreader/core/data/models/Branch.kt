package com.ecloud.apps.watermeterreader.core.data.models

import com.ecloud.apps.watermeterreader.core.database.model.BranchEntity
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkBranch

fun NetworkBranch.asEntity(): BranchEntity = BranchEntity(branchcode, branchname)
