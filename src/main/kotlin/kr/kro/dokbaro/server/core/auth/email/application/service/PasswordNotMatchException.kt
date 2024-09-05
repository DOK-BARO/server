package kr.kro.dokbaro.server.core.auth.email.application.service

import kr.kro.dokbaro.server.common.exception.http.status4xx.ForbiddenException

class PasswordNotMatchException : ForbiddenException("password not match")