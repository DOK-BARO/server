package kr.kro.dokbaro.server.domain.account.model.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Provider
import kr.kro.dokbaro.server.domain.account.model.Role
import kr.kro.dokbaro.server.domain.account.port.output.LoadAccountPort
import java.time.LocalDateTime

class AccountQueryServiceTest :
	StringSpec({
		val loadAccountPort = mockk<LoadAccountPort>()
		val accountQueryService = AccountQueryService(loadAccountPort)

		"socialId를 통한 조회를 진행한다" {
			val socialId = "adsf"
			val account =
				Account(
					7,
					socialId,
					Provider.KAKAO,
					setOf(Role.USER),
					LocalDateTime.now(),
				)
			every { loadAccountPort.findBy(socialId) } returns account

			val result = accountQueryService.getBy(socialId)
			
			result.id shouldBe account.id
			result.provider shouldBe account.provider.name
			result.role shouldBe account.roles.map { it.name }
		}

		"socialId와 일지하는 회원이 없을 시 예외를 발생한다" {
			val socialId = "adsf"
			every { loadAccountPort.findBy(socialId) } returns null

			shouldThrow<NotFoundAccountException> {
				accountQueryService.getBy(socialId)
			}
		}
	})