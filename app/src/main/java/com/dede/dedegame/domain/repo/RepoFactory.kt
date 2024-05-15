package com.dede.dedegame.domain.repo

import com.dede.dedegame.repo.DedeGameRepoImpl

object RepoFactory {

    fun getDedeGameRepo(): IDedeGameRepo{
        return DedeGameRepoImpl()
    }
}