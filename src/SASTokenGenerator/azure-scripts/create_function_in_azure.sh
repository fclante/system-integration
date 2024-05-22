#!/bin/bash

region=SwedenCentral
resourceGroup=school
storage_account_name=freyschoolstorage
app_name=schoolSASTokenGenerator

az functionapp create --resource-group $resourceGroup --consumption-plan-location $region --runtime dotnet-isolated --functions-version 4 --name $app_name --storage-account $storage_account_name --os-type Linux
