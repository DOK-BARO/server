package kr.kro.dokbaro.server.core.notification.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.entity.jooq.NotificationMapper
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationQueryRepository
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationRepository
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationVisibilityRepository
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.notificationFixture
import kr.kro.dokbaro.server.fixture.domain.notificationVisibilityFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class NotificationPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val notificationRepository = NotificationRepository(dslContext)
		val notificationQueryRepository = NotificationQueryRepository(dslContext, NotificationMapper())
		val notificationVisibilityRepository = NotificationVisibilityRepository(dslContext, NotificationMapper())

		val queryAdapter = NotificationPersistenceQueryAdapter(notificationQueryRepository)

		"알림 목록을 조회한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val targetSize = 3
			val notificationIds = (1..targetSize).map { notificationRepository.insert(notificationFixture()) }

			val visibilities =
				notificationIds.map { notificationId ->
					notificationVisibilityFixture(
						memberId = memberId,
						notificationId = notificationId,
					)
				}
			notificationVisibilityRepository.insertAll(visibilities)

			queryAdapter.findAllBy(memberId).size shouldBe targetSize
		}
	})