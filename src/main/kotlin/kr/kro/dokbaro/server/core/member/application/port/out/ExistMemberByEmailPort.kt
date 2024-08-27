package kr.kro.dokbaro.server.core.member.application.port.out

fun interface ExistMemberByEmailPort {
	fun existByEmail(email: String): Boolean
}