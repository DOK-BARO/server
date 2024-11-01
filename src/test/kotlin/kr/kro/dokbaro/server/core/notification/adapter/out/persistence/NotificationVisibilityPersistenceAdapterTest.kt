package kr.kro.dokbaro.server.core.notification.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.entity.jooq.NotificationMapper
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationRepository
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationVisibilityRepository
import kr.kro.dokbaro.server.core.notification.application.port.out.dto.LoadNotificationVisibilityCondition
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.notificationFixture
import kr.kro.dokbaro.server.fixture.domain.notificationVisibilityFixture
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.NotificationVisibilityDao

@PersistenceAdapterTest
class NotificationVisibilityPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val continuation: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val notificationRepository = NotificationRepository(dslContext)
		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val notificationVisibilityRepository = NotificationVisibilityRepository(dslContext, NotificationMapper())
		val notificationVisibilityDao = NotificationVisibilityDao(continuation)

		val adapter = NotificationVisibilityPersistenceAdapter(notificationVisibilityRepository)

		"알림 노출 내역을 전체 생성한다" {
			val notificationId = notificationRepository.insert(notificationFixture())
			val expectCount = 5

			val memberIds =
				(1..expectCount)
					.map {
						memberRepository.insert(memberFixture(email = Email("aaa$it@gmail.com")))
					}.map { it.id }

			adapter.insertAll(
				(1..expectCount).map {
					notificationVisibilityFixture(notificationId = notificationId, memberId = memberIds[it - 1])
				},
			)

			notificationVisibilityDao.findAll().size shouldBe expectCount
		}

		"알림 노출 내역 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val notificationId = notificationRepository.insert(notificationFixture())

			adapter.insertAll(listOf(notificationVisibilityFixture(notificationId = notificationId, memberId = memberId)))

			adapter
				.findAllBy(
					LoadNotificationVisibilityCondition(
						memberId,
						checked = false,
						disabled = false,
					),
				).size shouldBe 1
		}

		"알림 노출 내역을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val notificationId = notificationRepository.insert(notificationFixture())

			adapter.insertAll(listOf(notificationVisibilityFixture(notificationId = notificationId, memberId = memberId)))

			adapter.findBy(notificationId, memberId) shouldNotBe null
			adapter.findBy(notificationId + 10, memberId) shouldBe null
		}

		"알림 노출 내역을 수정한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val notificationId = notificationRepository.insert(notificationFixture())

			adapter.insertAll(listOf(notificationVisibilityFixture(notificationId = notificationId, memberId = memberId)))

			val notificationVisibility: NotificationVisibility = adapter.findBy(notificationId, memberId)!!

			notificationVisibility.check()

			adapter.update(notificationVisibility)

			val updatedNotificationVisibility: NotificationVisibility = adapter.findBy(notificationId, memberId)!!

			updatedNotificationVisibility.checked shouldBe true
		}
	})