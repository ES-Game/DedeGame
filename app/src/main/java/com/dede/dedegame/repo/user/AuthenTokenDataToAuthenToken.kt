package com.dede.dedegame.repo.user

import com.dede.dedegame.domain.model.AuthenToken
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Author


class AuthenTokenDataToAuthenToken: IConverter<AuthenTokenData, AuthenToken> {
    override fun convert(source: AuthenTokenData): AuthenToken {
        return AuthenToken().apply {
            this.accessToken = source.accessToken
            this.refreshToken = source.refreshToken
            this.expiresIn = source.expiresIn
            this.tokenType = source.tokenType
        }
    }
}