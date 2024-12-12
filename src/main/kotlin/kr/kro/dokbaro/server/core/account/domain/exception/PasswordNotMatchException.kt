package kr.kro.dokbaro.server.core.account.domain.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException

class PasswordNotMatchException : UnauthorizedException("Password not match")