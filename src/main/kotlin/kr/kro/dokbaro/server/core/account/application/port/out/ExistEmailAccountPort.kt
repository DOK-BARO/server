package kr.kro.dokbaro.server.core.account.application.port.out

/**
 * email 계정 존재 여부를 확인하는 port 입니다.
 */
fun interface ExistEmailAccountPort {
	fun existsByEmail(email: String): Boolean
}