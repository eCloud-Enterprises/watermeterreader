package com.ecloud.apps.watermeterreader.core.data.testdoubles

import com.ecloud.apps.watermeterreader.core.model.data.ScannedAudit
import com.ecloud.apps.watermeterreader.core.network.WmrNetworkDataSource
import com.ecloud.apps.watermeterreader.core.network.dto.GetStockAuditsWithQtyResponseDto
import com.ecloud.apps.watermeterreader.core.network.dto.LoginDto
import com.ecloud.apps.watermeterreader.core.network.dto.NetworkBranch
import com.ecloud.apps.watermeterreader.core.network.dto.ResponseDto
import com.ecloud.apps.watermeterreader.core.network.dto.WarehouseDto

class TestWmrNetworkDataSource : WmrNetworkDataSource {
    override suspend fun getProjects(objectCode: String, type: String): List<WarehouseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun login(userid: String, password: String): LoginDto {
        TODO("Not yet implemented")
    }

    override suspend fun postStockAuditQty(
        docNo: String,
        itemCode: String,
        quantity: Double
    ): ResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun postStockAuditQty(stocks: List<ScannedAudit>): ResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun checkProjects(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun checkStockAudits(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getStockAudits(
        warehouse: String,
        objectCode: String,
        type: String
    ): List<StockAuditDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getStockAuditsWithQty(
        warehouse: String,
        objectCode: String,
        type: String
    ): GetStockAuditsWithQtyResponseDto {
        TODO("Not yet implemented")
    }

    override suspend fun getBranches(objectCode: String, type: String): List<NetworkBranch> {
        TODO("Not yet implemented")
    }
}
