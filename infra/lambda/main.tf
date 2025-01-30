resource "aws_lambda_function" "my_lambda" {
    function_name = "report-generator-lambda"
    role = var.lamda_role_arn
    handler = "com.sks.reportFn.handler.ReportLambdaHandler::handleRequest"
    runtime       = "java17"
    timeout       = 30
    memory_size   = 512
    s3_bucket     = var.lamda_bucket_id
    s3_key        = "report-lambda-function.jar"
    
    vpc_config {
    subnet_ids         = [var.private_subnet_1_id, var.private_subnet_2_id]
    security_group_ids = [var.rds_sg_id]
  } 
  tags = {
      Name = "report-lamda"
  }
}