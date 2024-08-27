package kr.kro.dokbaro.server.core.member.application.port.input.command

import java.util.UUID

fun interface CheckMemberEmailUseCase {
	fun checkMail(certificationId: UUID)
}