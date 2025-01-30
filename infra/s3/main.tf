resource "aws_s3_bucket" "lambda_bucket" {
  bucket = "com.sks.lamda-bucket"
  tags = {
      Name = "report-s3"
  }
}