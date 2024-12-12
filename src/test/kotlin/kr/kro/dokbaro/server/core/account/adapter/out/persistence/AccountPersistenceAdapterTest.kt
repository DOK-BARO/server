package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.domain.AccountPassword
import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import kr.kro.dokbaro.server.core.account.domain.SocialAccount
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.AccountPasswordDao
import org.jooq.generated.tables.daos.Oauth2AccountDao

@PersistenceAdapterTest
class AccountPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
		val accountRepository = AccountRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val oauth2AccountDao = Oauth2AccountDao(configuration)
		val accountPasswordDao = AccountPasswordDao(configuration)

		val adapter = AccountPersistenceAdapter(accountRepository)

		val accountQueryRepository = AccountQueryRepository(dslContext)

		"소셜 계정을 추가한다" {
			val member = memberRepository.insert(memberFixture())

			adapter.insertSocialAccount(
				SocialAccount(
					socialId = "socialId",
					provider = AuthProvider.GITHUB,
					memberId = member.id,
				),
			)

			oauth2AccountDao.findAll().size shouldBe 1
		}

		"계정 비밀번호를 추가한다" {
			val member = memberRepository.insert(memberFixture())

			adapter.insertAccountPassword(
				AccountPassword(
					password = "password",
					memberId = member.id,
				),
			)

			accountPasswordDao.findAll().size shouldBe 1
		}

		"계정 업데이트를 추가한다" {
			val member = memberRepository.insert(memberFixture())

			adapter.insertAccountPassword(
				AccountPassword(
					password = "password",
					memberId = member.id,
				),
			)

			val targetPassword: AccountPassword = accountQueryRepository.findByMemberId(member.id)!!

			targetPassword.password = "newPassword"

			adapter.updateAccountPassword(targetPassword)

			val savedPassword: AccountPassword = accountQueryRepository.findByMemberId(member.id)!!

			savedPassword.password shouldBe targetPassword.password
		}
	})