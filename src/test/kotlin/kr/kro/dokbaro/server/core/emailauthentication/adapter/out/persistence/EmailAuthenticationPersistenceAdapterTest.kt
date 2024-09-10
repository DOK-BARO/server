package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.entity.jooq.EmailAuthenticationMapper
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq.EmailAuthenticationQueryRepository
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq.EmailAuthenticationRepository
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.EmailAuthenticationDao

@PersistenceAdapterTest
class EmailAuthenticationPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val repository = EmailAuthenticationRepository(dslContext)
		val queryRepository = EmailAuthenticationQueryRepository(dslContext, EmailAuthenticationMapper())
		val adapter =
			EmailAuthenticationPersistenceAdapter(repository, queryRepository)

		val emailAuthenticationDao = EmailAuthenticationDao(configuration)

		"저장을 수행한다" {
			val emailAuthentication =
				EmailAuthentication(
					"www@example.com",
					"ADSFGH",
				)

			val id = adapter.save(emailAuthentication)

			emailAuthenticationDao.findAll().shouldNotBeEmpty()
			id shouldNotBe null
		}

		"수정을 수행한다" {
			val emailAuthentication =
				EmailAuthentication(
					"www@example.com",
					"ADSFGH",
				)

			val id = adapter.save(emailAuthentication)

			val newCode = "HELLO9"
			val newEmailAuthentication =
				EmailAuthentication(
					address = "www@example.com",
					code = newCode,
					id = id,
				)

			adapter.update(newEmailAuthentication)

			val result = emailAuthenticationDao.findById(id)!!

			result.code shouldBe newCode
		}

		"조회를 수행한다" {
			val address = "www@example.com"
			val code = "ADSFGH"
			val emailAuthentication =
				EmailAuthentication(
					address,
					code,
					authenticated = true,
					used = false,
				)

			adapter.save(emailAuthentication)

			adapter.findBy(
				SearchEmailAuthenticationCondition(),
			) shouldNotBe null

			adapter.findBy(
				SearchEmailAuthenticationCondition(
					address,
					code,
				),
			) shouldNotBe null

			adapter.findBy(
				SearchEmailAuthenticationCondition(
					authenticated = true,
					used = false,
				),
			) shouldNotBe null

			adapter.findBy(
				SearchEmailAuthenticationCondition(
					authenticated = false,
					used = false,
				),
			) shouldBe null

			adapter.findBy(
				SearchEmailAuthenticationCondition(
					address,
					code,
					authenticated = false,
					used = false,
				),
			) shouldBe null
		}

		"존재 여부를 확인한다" {
			val address = "www@example.com"
			val code = "ADSFGH"
			val emailAuthentication =
				EmailAuthentication(
					address,
					code,
					authenticated = true,
					used = false,
				)

			adapter.save(emailAuthentication)

			adapter.existBy(
				SearchEmailAuthenticationCondition(),
			) shouldBe true

			adapter.existBy(
				SearchEmailAuthenticationCondition(
					address,
					code,
				),
			) shouldBe true

			adapter.existBy(
				SearchEmailAuthenticationCondition(
					authenticated = true,
					used = false,
				),
			) shouldBe true

			adapter.existBy(
				SearchEmailAuthenticationCondition(
					authenticated = false,
					used = false,
				),
			) shouldBe false

			adapter.existBy(
				SearchEmailAuthenticationCondition(
					address,
					code,
					authenticated = false,
					used = false,
				),
			) shouldBe false
		}
	})