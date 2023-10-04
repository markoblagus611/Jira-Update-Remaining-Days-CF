# Jira Calculate Remaining Days Custom Field Value

This script is designed to work as a scheduled job in Jira, automating the updating of a custom field called 'Ostalo dana' every day.

## How It Works

1. **Retrieving All Issues**: The script begins by retrieving the total number of issues in Jira based on a specified JQL query.

2. **Batch Processing**: It then retrieves issues in batches of 50 until all issues have been processed. This batching helps manage large sets of issues efficiently.

3. **Updating Custom Field**: For each issue, the script calculates the difference between the custom field called "Rok dobave" and today's date and updates the 'Ostalo dana' custom field with this value.

## Usage

To use this script:

1. Customize the JQL query in the script to match your specific criteria. This query determines which issues the script will update.

2. Set up the script as a scheduled job in your Jira instance to run daily.

## Example JQL Query

```sql
project = "IT Support" AND status = "In Progress"