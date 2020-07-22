#!/bin/bash -x
# This script bootstraps a debian/ubuntu server
# with the latest ansible version and a few roles
# for a base jenkins & jenkins job builder configuration
# wr: tjgornick@gmail.com | v: 2.0.0


# Let's get started
apt-get update && \
  apt-get install software-properties-common -y && \
  apt-add-repository ppa:ansible/ansible -y && \
  apt-get update && \
  apt-get install apt ansible vim curl sudo python-pip -y

# Using Ansible Galaxy for base roles
# for jenkins: https://galaxy.ansible.com/geerlingguy/jenkins/
ansible-galaxy install geerlingguy.jenkins

# for jjb: https://galaxy.ansible.com/openstack/jenkins-job-builder/
ansible-galaxy install openstack.jenkins-job-builder

# Let's tell the ansible hosts file to talk to ourselves
echo "[jenkins]" >> /etc/ansible/hosts
echo "localhost ansible_connection=local" >> /etc/ansible/hosts

# Copy our site.yaml into place
cp ansible/site.yaml /etc/ansible/

# Copy our jenkins settings to a place site.yaml will look
cp jenkins/security_settings.groovy /etc/ansible/
cp -r jjb/ /etc/ansible/

# Give Ansible a go
ansible-playbook /etc/ansible/site.yaml
