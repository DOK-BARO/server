package kr.kro.dokbaro.server.core.emailauthentication.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class AlreadySignUpException : BadRequestException("이미 회원가입이 진행된 이메일입니다.")