// Import necessary Java time libraries for date operations
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// Get the current date
def today = LocalDate.now()

// Initialize the start index for pagination
def startIndex = 0

// Retrieving the number of total issues using JQL query
def response = get('/rest/api/2/search')
        .queryString('jql', 'YOUR_SPECIFIC_JQL_QUERY_HERE') // Customize this JQL query
        .queryString('startAt', startIndex)
        .queryString('maxResults', 50)
        .header('Content-Type', 'application/json')
        .asObject(Map)

def totalIssues = response.body.total + 50

// Loop through all issues (pagination)
for (def startIndex2 = 0; startIndex2 <= totalIssues; startIndex2 += 50) {
    logger.info("Start index: " + startIndex2)

    // Retrieve 50 issues using JQL query (customize as needed)
    def response2 = get('/rest/api/2/search')
            .queryString('jql', 'YOUR_SPECIFIC_JQL_QUERY_HERE') // Customize this JQL query
            .queryString('startAt', startIndex2)
            .queryString('maxResults', 50)
            .header('Content-Type', 'application/json')
            .asObject(Map)

    if (response2.body.issues.size() > 0) {
        // Loop through 50 issues and update the custom field of each issue
        for (int issueCount = 0; issueCount < response2.body.issues.size(); issueCount++) {
            def issueKey = response2.body.issues[issueCount].key
            def rokDobaveString = response2.body.issues[issueCount].fields.customfield_10063

            // Parse the value of custom field from String to Date
            def rokDobaveDate = LocalDate.parse(rokDobaveString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            def differenceInDays = ChronoUnit.DAYS.between(today, rokDobaveDate)

            // Update the custom field with the calculated difference in days
            def updateIssue = put('/rest/api/2/issue/' + issueKey)
                    .header('Content-Type', 'application/json')
                    .body([
                            fields: [
                                    customfield_id: differenceInDays
                            ]
                    ])
                    .asString()
        }
    } else {
        logger.info("There are no issues")
    }
}
