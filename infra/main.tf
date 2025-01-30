terraform {
  required_version = ">= 1.3.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

module "networking" {
  source                = "./vpc"
  vpc_cidr              = var.vpc_cidr
  public_subnet_cidr    = var.public_subnet_cidr
  private_subnet_cidr_1 = var.private_subnet_cidr_1
  private_subnet_cidr_2 = var.private_subnet_cidr_2
}

module "security" {
  source = "./security"
  vpc_id = module.networking.vpc_id
}

module "ec2_jump_box" {
  source         = "./ec2_jump_box"
  subnet_id      = module.networking.public_subnet_id
  jump_box_sg_id = module.security.jump_box_sg_id
}

module "rds" {
  source              = "./rds"
  private_subnet_1_id = module.networking.private_subnet_1_id
  private_subnet_2_id = module.networking.private_subnet_2_id
  security_grp_id     = module.security.sg_id
}

module "s3" {
  source = "./s3"
}

module "iam" {
  source = "./iam"
}

module "lambda" {
  source              = "./lambda"
  lamda_role_arn      = module.iam.lamda_role_arn
  lamda_bucket_id     = module.s3.bucket_id
  private_subnet_1_id = module.networking.private_subnet_1_id
  private_subnet_2_id = module.networking.private_subnet_2_id
  rds_sg_id           = module.security.sg_id
}

module "scheduler" {
  source    = "./scheduler"
  lamda_arn = module.lambda.lamda_arn
}


