package kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.entity.jooq.EmailAccountMapper
import kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.repository.jooq.AccountPasswordRepository
import kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.repository.jooq.EmailCertificatedAccountQueryRepository
import kr.kro.dokbaro.server.core.auth.email.domain.AccountPassword
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import org.jooq.Configuration
import org.jooq.DSLContext

@PersistenceAdapterTest
class EmailAccountPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val accountPasswordRepository = AccountPasswordRepository(dslContext)
		val queryRepository = EmailCertificatedAccountQueryRepository(dslContext, EmailAccountMapper())

		val adapter = EmailAccountPersistenceAdapter(accountPasswordRepository, queryRepository)

		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val member =
			Member(
				"nick",
				Email("www4@example.com"),
				"profile.png",
				UUIDUtils.stringToUUID("2b946c7f-abd1-4aef-a440-5d7670e4db45"),
			)

		"저장을 수행한다" {
			val savedMember: Member = memberRepository.insert(member)
			val savedId = adapter.insert(AccountPassword("password", savedMember.id))

			savedId shouldNotBe null
		}

		"email을 통한 조회를 수행한다" {
			val savedMember: Member = memberRepository.insert(member)
			val password = "password"
			adapter.insert(AccountPassword(password, savedMember.id))

			val result = adapter.findByEmail(savedMember.email.address)
			result shouldNotBe null
			result?.password shouldBe password
			result?.certificationId shouldBe savedMember.certificationId
			adapter.findByEmail("whoAreYou@asd.com") shouldBe null
		}
	})