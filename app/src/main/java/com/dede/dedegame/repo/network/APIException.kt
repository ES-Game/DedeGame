package com.dede.dedegame.repo.network

class APIException : Exception {
    var code = 0
        private set
    var payload: Any? = null
        private set

    constructor(code: Int) : super() {
        this.code = code
    }

    constructor(message: String?) : super(message) {}
    constructor(message: String?, code: Int) : super(message) {
        this.code = code
    }

    constructor(message: String?, code: Int, payload: Any?) : super(message) {
        this.code = code
        this.payload = payload
    }

    constructor(message: String?, t: Throwable?) : super(message, t) {}
    constructor(code: Int, t: Throwable?) : super(t) {
        this.code = code
    }

    constructor(t: Throwable?) : super(t) {}

    companion object {

        const val BODY_NULL_ERROR = 901
        const val BODY_UNSUCCESS_ERROR = 902
        const val NETWORK_ERROR = 900
        const val SERVER_ERROR = 903

    }
}