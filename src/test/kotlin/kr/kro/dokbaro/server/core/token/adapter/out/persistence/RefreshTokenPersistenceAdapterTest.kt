package kr.kro.dokbaro.server.core.token.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.token.adapter.out.persistence.entity.RefreshTokenMapper
import kr.kro.dokbaro.server.core.token.adapter.out.persistence.repository.RefreshTokenRepository
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.DSLContext
import java.time.LocalDateTime
import java.util.UUID

@PersistenceAdapterTest
class RefreshTokenPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val refreshTokenRepository = RefreshTokenRepository(dslContext, RefreshTokenMapper())

		val adapter = RefreshTokenPersistenceAdapter(refreshTokenRepository)

		"생성 및 조회를 수행한다" {
			val member: Member = memberRepository.insert(memberFixture())

			val token = UUID.randomUUID().toString()
			val refreshToken =
				RefreshToken(
					token = token,
					certificateId = member.certificationId,
					expiredAt = LocalDateTime.now(),
				)

			adapter.insert(refreshToken)

			val loadByToken: RefreshToken = adapter.loadByToken(token)!!

			loadByToken.token shouldBe token
			loadByToken.certificateId shouldBe member.certificationId
		}

		"업데이트를 수행한다" {
			val member: Member = memberRepository.insert(memberFixture())

			val token = UUID.randomUUID().toString()
			val refreshToken =
				RefreshToken(
					token = token,
					certificateId = member.certificationId,
					expiredAt = LocalDateTime.now(),
				)

			adapter.insert(refreshToken)

			refreshToken.use()

			adapter.update(refreshToken)

			val loadByToken: RefreshToken = adapter.loadByToken(refreshToken.token)!!

			loadByToken.used shouldBe true
		}

		"삭제를 수행한다" {
			val member: Member = memberRepository.insert(memberFixture())

			val token = UUID.randomUUID().toString()
			val refreshToken =
				RefreshToken(
					token = token,
					certificateId = member.certificationId,
					expiredAt = LocalDateTime.now(),
				)

			adapter.insert(refreshToken)

			adapter.deleteBy(refreshToken.certificateId)

			adapter.loadByToken(refreshToken.token) shouldBe null
		}
	})