package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class AccountPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val accountRepository = AccountRepository(dslContext)

		val queryAdapter = EmailAccountPersistenceQueryAdapter(AccountQueryRepository(dslContext))

		"member id 혹은 email을 통한 password 조회를 수행한다" {
			val member = memberRepository.insert(memberFixture())

			accountRepository.insertEmailAccount(
				EmailAccount(
					email = "email@gmail.com",
					password = "password",
					memberId = member.id,
				),
			)

			queryAdapter.findByEmail(member.email!!.address) shouldNotBe null
			queryAdapter.findByMemberId(member.id) shouldNotBe null
		}
	})