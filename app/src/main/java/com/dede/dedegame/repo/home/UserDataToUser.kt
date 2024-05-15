package com.dede.dedegame.repo.home

import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Author
import com.dede.dedegame.domain.model.User
import com.dede.dedegame.repo.user.UserData


class UserDataToUser: IConverter<UserData, User> {
    override fun convert(source: UserData): User {
        return User().apply {
            this.id = source.id
            this.name = source.name
            this.active = source.active
            this.provider = source.provider
            this.fullName = source.fullName
            this.email = source.email
            this.birthday = source.birthday
            this.phone = source.phone
            this.address = source.address
            this.gender = source.gender
            this.nationalId = source.nationalId
            this.nationalIdIssuedDate = source.nationalIdIssuedDate
            this.nationalIdIssuedLocation = source.nationalIdIssuedLocation
            this.emailVerifiedAt = source.emailVerifiedAt
            this.playNowLimitationMethod = source.playNowLimitationMethod
            this.coin = source.coin
            this.isNew = source.isNew
            this.dedeToken = source.dedeToken
            this.createdAt = source.createdAt
        }
    }
}