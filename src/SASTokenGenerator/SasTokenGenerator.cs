using System;
using System.Security.Cryptography;
using System.Text;

namespace SASTokenGenerator
{
    public class SasTokenGenerator
    {
        public static string GenerateSasToken(string resourceUri, string keyName, string key, int expiryInMinutes = 60)
        {
            var expiry = DateTimeOffset.UtcNow.AddMinutes(expiryInMinutes).ToUnixTimeSeconds();
            string stringToSign = Uri.EscapeDataString(resourceUri) + "\n" + expiry;
            string signature;

            using (var hmac = new HMACSHA256(Encoding.UTF8.GetBytes(key)))
            {
                signature = Convert.ToBase64String(hmac.ComputeHash(Encoding.UTF8.GetBytes(stringToSign)));
            }

            var sasToken = $"SharedAccessSignature sr={Uri.EscapeDataString(resourceUri)}&sig={Uri.EscapeDataString(signature)}&se={expiry}&skn={keyName}";
            return sasToken;
        }
    }

}
