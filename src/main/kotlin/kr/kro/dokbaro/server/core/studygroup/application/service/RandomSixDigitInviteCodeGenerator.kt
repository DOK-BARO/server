package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode
import org.springframework.stereotype.Component

@Component
class RandomSixDigitInviteCodeGenerator : InviteCodeGenerator {
	override fun generate(): InviteCode =
		InviteCode(
			(1..6)
				.map { "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".random() }
				.joinToString(""),
		)
}