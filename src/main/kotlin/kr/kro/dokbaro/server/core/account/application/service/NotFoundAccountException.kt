package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.global.exception.http.status4xx.NotFoundException

class NotFoundAccountException : NotFoundException("Account not found")