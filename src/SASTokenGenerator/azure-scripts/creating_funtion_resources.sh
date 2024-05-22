#!/bin/bash

location=SwedenCentral
resourceGroup=school
storage_account_name=freyschoolstorage
# Login to Azure
az login

# Create a resource group
# Im using a preexisting resource group - uncomment the line below to create a new one
#az group create --name $resourceGroup --location $location

az storage account create --name $storage_account_name --location $location --resource-group $resourceGroup --sku Standard_LRS --allow-blob-public-access false