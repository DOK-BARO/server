package kr.kro.dokbaro.server.core.account.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class AccountNotFoundException : NotFoundException("account not found")