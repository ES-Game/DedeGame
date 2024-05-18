package com.dede.dedegame.repo.temp.mainGame

import com.dede.dedegame.domain.model.mainGame.Pagination
import com.dede.dedegame.repo.convert.IConverter


class PaginationDataToPagination: IConverter<PaginationData, Pagination> {
    override fun convert(source: PaginationData): Pagination {
        return Pagination().apply {
            this.total = source.total
            this.currentPage = source.currentPage
            this.lastPage = source.lastPage
            this.perPage = source.perPage
        }
    }
}