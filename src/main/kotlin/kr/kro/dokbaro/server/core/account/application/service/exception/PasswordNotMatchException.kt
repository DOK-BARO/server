package kr.kro.dokbaro.server.core.account.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException

/**
 * password가 일치하지 않으면 발생하는 exception 입니다.
 */
class PasswordNotMatchException : UnauthorizedException("Password not match")