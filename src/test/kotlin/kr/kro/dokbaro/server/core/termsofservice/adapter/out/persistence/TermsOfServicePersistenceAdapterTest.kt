package kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq.TermsOfServiceRepository
import kr.kro.dokbaro.server.core.termsofservice.domain.AgreeTermsOfService
import kr.kro.dokbaro.server.core.termsofservice.domain.TermsOfService
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.AgreeTermsOfServiceDao
import org.jooq.generated.tables.daos.TermsOfServiceDetailDao
import org.jooq.generated.tables.pojos.TermsOfServiceDetail
import java.time.LocalDateTime

@PersistenceAdapterTest
class TermsOfServicePersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val termsOfServiceRepository = TermsOfServiceRepository(dslContext)
		val adapter = TermsOfServicePersistenceAdapter(termsOfServiceRepository)

		val agreeTermsOfServiceDao = AgreeTermsOfServiceDao(configuration)
		val termsOfServiceDetailDao = TermsOfServiceDetailDao(configuration)

		"이용 약관 동의 항목을 저장한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			adapter.insertAgree(AgreeTermsOfService(memberId, TermsOfService.entries))

			agreeTermsOfServiceDao.findAll().size shouldBe TermsOfService.entries.size
		}

		"상세 내용을 조회한다" {
			val target: Long = 1
			termsOfServiceDetailDao.insert(
				TermsOfServiceDetail(1, target, "내용".toByteArray(), LocalDateTime.now(), LocalDateTime.now(), false),
			)

			adapter.getDetail(target) shouldNotBe null
			adapter.getDetail(67777) shouldBe null
		}
	})