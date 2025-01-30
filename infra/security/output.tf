output "sg_id" {
  value = aws_security_group.rds_sg.id
}

output "jump_box_sg_id" {
  value = aws_security_group.jump_box_sg.id
}