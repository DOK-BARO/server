package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.global.exception.http.status4xx.NotFoundException

class NotFoundAccountException : NotFoundException("Account not found")