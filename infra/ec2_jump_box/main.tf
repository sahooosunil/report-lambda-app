resource "aws_instance" "report_jump_box" {
  tags = {
    "Name" = "report-jump-box" 
  }
  subnet_id = var.subnet_id
  security_groups = [ var.jump_box_sg_id ]
  instance_type = "t2.micro"
  ami = "ami-053b12d3152c0cc71"
  key_name = "todo-key-pair"
  associate_public_ip_address = true
  provisioner "remote-exec" {
    connection {
        type = "ssh"
        user = "ubuntu"
        private_key = file("/Users/sunilsahu/Downloads/todo-key-pair.pem")
        host = self.public_ip
    }

    inline = [ 
        "sudo apt update -y",
        "sudo apt upgrade -y",
        "sudo apt install mysql-client -y",
        "sudo apt install openjdk-17-jdk -y",
        "sudo apt install maven -y"
     ]
  }
}
