package kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq.TermsOfServiceQueryRepository
import kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq.TermsOfServiceRepository
import kr.kro.dokbaro.server.core.termsofservice.domain.AgreeTermsOfService
import kr.kro.dokbaro.server.core.termsofservice.domain.TermsOfService
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class TermsOfServicePersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val termsOfServiceRepository = TermsOfServiceRepository(dslContext)

		val termsOfServiceQueryRepository = TermsOfServiceQueryRepository(dslContext)

		val queryAdapter = TermsOfServicePersistenceQueryAdapter(termsOfServiceQueryRepository)

		"member의 동의 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			termsOfServiceRepository.insertAgreeTermsOfService(AgreeTermsOfService(memberId, TermsOfService.entries))

			queryAdapter.findAll(memberId).size shouldBe TermsOfService.entries.size
		}
	})