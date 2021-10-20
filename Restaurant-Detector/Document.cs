using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SentimentAnalysisAPI
{
    public class Document
    {
        [JsonProperty("text")]
        public string Text { get; set; }
    }
}
