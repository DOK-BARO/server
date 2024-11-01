package kr.kro.dokbaro.server.core.notification.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.notification.adapter.out.persistence.repository.jooq.NotificationRepository
import kr.kro.dokbaro.server.fixture.domain.notificationFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class NotificationPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))
	
		val notificationRepository = NotificationRepository(dslContext)

		val adapter = NotificationPersistenceAdapter(notificationRepository)

		"알림을 생성한다" {
			adapter.insert(notificationFixture()) shouldNotBe null
		}
	})