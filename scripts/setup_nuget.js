const path = require('path');
const fs = require('fs');

class SetupDotnet {
    static setup() {
        console.log('******* Running setup_dotnet_def.js ********');

        console.log('\nGenerating github nuget.config');

        const user = process.env.GHP_USER;
        const token = process.env.GHP_TOKEN;
        const file = `<?xml version="1.0" encoding="utf-8"?>
    <configuration>
        <packageSources>
            <add key="nuget.org" value="https://api.nuget.org/v3/index.json" />
            <add key="github" value="https://nuget.pkg.github.com/fclante/index.json" />
        </packageSources>
        <packageSourceCredentials>
            <github>
                <add key="Username" value="${user}" />
                <add key="ClearTextPassword" value="${token}" />
            </github>
        </packageSourceCredentials>
    </configuration>`;

        fs.writeFileSync(path.join(__dirname, '../src/nuget.config'), file);

        console.log("\ndone");

        process.exit(0);
    }
}

SetupDotnet.setup();