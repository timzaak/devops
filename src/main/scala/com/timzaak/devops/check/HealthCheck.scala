package com.timzaak.devops.check

trait HealthCheck {
  def check(): Boolean
}
