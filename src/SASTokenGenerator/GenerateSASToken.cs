using Microsoft.Azure.Functions.Worker;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;


namespace SASTokenGenerator
{
    public class GenerateSASToken
    {
        private readonly ILogger<GenerateSASToken> _logger;

        public GenerateSASToken(ILogger<GenerateSASToken> logger)
        {
            _logger = logger;
        }

        [Function("GenerateSASToken")]
        public IActionResult Run([HttpTrigger(AuthorizationLevel.Anonymous, "get", "post")] HttpRequest req)
        {
            _logger.LogInformation("Received a request to generate a SAS token");

            string namespaceUrl = "https://frey-school.servicebus.windows.net/tracker";
            string keyName = "ReadWriteToEventHub";
            string key = "[REDACTED]";
            string sasToken = SasTokenGenerator.GenerateSasToken(namespaceUrl, keyName, key);

            return new OkObjectResult(new { token = sasToken });
        }
    }
}
