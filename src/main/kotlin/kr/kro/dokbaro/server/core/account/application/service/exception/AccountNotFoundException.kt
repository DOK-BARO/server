package kr.kro.dokbaro.server.core.account.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

/**
 * 계정 조회 시 찾을 수 없으면 발생하는 Exception 입니다.
 */
class AccountNotFoundException : NotFoundException("account not found")