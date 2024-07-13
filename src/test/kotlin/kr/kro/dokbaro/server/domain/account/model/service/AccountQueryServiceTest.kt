package kr.kro.dokbaro.server.domain.account.model.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Role
import kr.kro.dokbaro.server.domain.account.port.output.LoadAccountPort
import kr.kro.dokbaro.server.global.AuthProvider
import java.time.LocalDateTime

class AccountQueryServiceTest :
	StringSpec({
		val loadAccountPort = mockk<LoadAccountPort>()
		val accountQueryService = AccountQueryService(loadAccountPort)

		"Id를 통한 조회를 진행한다" {
			val id = "adsf"
			val account =
				Account(
					7,
					id,
					AuthProvider.KAKAO,
					setOf(Role.USER),
					LocalDateTime.now(),
				)
			every { loadAccountPort.findBy(id) } returns account

			val result = accountQueryService.getById(id)
			
			result.id shouldBe account.id
			result.provider shouldBe account.provider.name
			result.role shouldBe account.roles.map { it.name }
		}

		"Id와 일지하는 회원이 없을 시 예외를 발생한다" {
			val id = "adsf"
			every { loadAccountPort.findBy(id) } returns null

			shouldThrow<NotFoundAccountException> {
				accountQueryService.getById(id)
			}
		}
	})