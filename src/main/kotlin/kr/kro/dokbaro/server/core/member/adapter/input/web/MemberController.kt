package kr.kro.dokbaro.server.core.member.adapter.input.web

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.adapter.input.web.dto.ModifyMemberRequest
import kr.kro.dokbaro.server.core.member.application.port.input.command.ModifyMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.WithdrawMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.ModifyMemberCommand
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
	private val modifyMemberUseCase: ModifyMemberUseCase,
	private val withdrawMemberUseCase: WithdrawMemberUseCase,
) {
	@PutMapping("/login-user")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun modifyMember(
		auth: Authentication,
		@RequestBody request: ModifyMemberRequest,
	) {
		modifyMemberUseCase.modify(
			ModifyMemberCommand(
				certificationId = UUIDUtils.stringToUUID(auth.name),
				nickname = request.nickname,
				email = request.email,
				profileImage = request.profileImage,
			),
		)
	}

	@GetMapping("/login-user")
	fun getLoginUser(
		@Login user: DokbaroUser,
	): CertificatedMember =
		CertificatedMember(
			id = user.id,
			certificationId = user.certificationId,
			nickname = user.nickname,
			email = user.email,
			role = user.role,
		)

	@PostMapping("/withdraw")
	fun withdraw(auth: Authentication) {
		withdrawMemberUseCase.withdrawBy(UUIDUtils.stringToUUID(auth.name))
	}
}