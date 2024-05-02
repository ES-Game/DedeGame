package com.quangph.dedegame.domain.repo

import com.quangph.dedegame.repo.DedeGameRepoImpl

object RepoFactory {

    fun getDedeGameRepo(): IDedeGameRepo{
        return DedeGameRepoImpl()
    }
}