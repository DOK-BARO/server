package kr.kro.dokbaro.server.dummy

import org.springframework.context.ApplicationEventPublisher

class EventPublisherDummy : ApplicationEventPublisher {
	override fun publishEvent(event: Any) {
	}
}