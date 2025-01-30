resource "aws_cloudwatch_event_rule" "midnight_trigger" {
  name                = "daily-midnight-trigger"
  schedule_expression = "cron(0 0 * * ? *)"
}

resource "aws_cloudwatch_event_target" "lambda_target" {
  rule      = aws_cloudwatch_event_rule.midnight_trigger.name
  arn       = var.lamda_arn
}