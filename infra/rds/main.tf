resource "aws_db_instance" "my_rds" {
    instance_class = "db.t3.micro"
    allocated_storage = 20
    engine_version      = "8.0"
    engine              = "mysql"
    username           = "admin"
    password           = "Password123!"
    db_subnet_group_name = aws_db_subnet_group.rds_subnet_group.name
    vpc_security_group_ids = [var.security_grp_id]
    publicly_accessible = false
    multi_az = true
    final_snapshot_identifier = "foo"
    skip_final_snapshot = true
    tags = {
      Name = "report-db"
    }
}

resource "aws_db_subnet_group" "rds_subnet_group" {
  name = "rds_subnet_group"
  subnet_ids = [var.private_subnet_1_id, var.private_subnet_2_id]
}