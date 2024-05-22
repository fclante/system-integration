ARG VARIANT=8.0-bookworm-slim
FROM mcr.microsoft.com/dotnet/sdk:${VARIANT}
ENV PATH $PATH:/home/vscode/.dotnet:/home/vscode/.dotnet/tools

# Clear this environment variable so XML docs from NuGet packages are unpacked
ENV NUGET_XMLDOC_MODE=

# Upgrade packages due to mentioned CVEs
RUN apt-get update && apt-get install -y gpg lsb-release wget curl

# Install Azure Functions Core Tools and PowerShell for amd64
RUN if [ "$(dpkg --print-architecture)" = "amd64" ]; then \
        apt-get update && \
        curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg && \
        mv microsoft.gpg /etc/apt/trusted.gpg.d/microsoft.gpg && \
        sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/debian/$(lsb_release -rs | cut -d'.' -f 1)/prod $(lsb_release -cs) main" > /etc/apt/sources.list.d/dotnetdev.list' && \
        apt-get update && apt-get install -y azure-functions-core-tools-4 && \
        POWERSHELL_FILE_NAME="powershell_7.4.1-1.deb_amd64.deb" && \
        wget https://github.com/PowerShell/PowerShell/releases/download/v7.4.1/${POWERSHELL_FILE_NAME} && \
        dpkg -i ${POWERSHELL_FILE_NAME} && \
        apt-get install -f && \
        rm ${POWERSHELL_FILE_NAME}; \
    fi

# Install PowerShell for arm64
RUN if [ "$(dpkg --print-architecture)" = "arm64" ]; then \
        apt-get update && \
        apt-get install -y curl tar && \
        POWERSHELL_FILE_PATH="/opt/microsoft/powershell/7" && \
        curl -L -o /tmp/powershell.tar.gz https://github.com/PowerShell/PowerShell/releases/download/v7.4.1/powershell-7.4.1-linux-arm64.tar.gz && \
        mkdir -p ${POWERSHELL_FILE_PATH} && \
        tar zxf /tmp/powershell.tar.gz -C ${POWERSHELL_FILE_PATH} && \
        chmod +x ${POWERSHELL_FILE_PATH}/pwsh && \
        ln -snf ${POWERSHELL_FILE_PATH}/pwsh /usr/bin/pwsh && \
        rm /tmp/powershell.tar.gz; \
    fi

# Additional Dockerfile instructions (if any)...
