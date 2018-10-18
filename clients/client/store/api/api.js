/**
 * APIs Request Url
 * Version 0.1
 */
const host = 'http://10.220.132.186'

const version = '/api/0.1'

const port2 = ':8081'

// Dashboard API
export const GET_CI_DEPLOYMENTS_TYPE = host + version + '/ci-deployments/stats/failures/by?category={0}&pipeline={1}&name={2}&start={3}&end={4}'

export const GET_DURATION = host + version + '/ci-stats/search/pipeline?name={0}&size=10&page=0&sort=triggerId,desc'

export const GET_TEST_SCENARIO = host + version + '/ci-tests/stats/{0}?limit=10'

export const GET_LATEST_TEST_SCENARIO = host + version + '/ci-tests/stats/{pipeline}/{triggerId}'

export const GET_LATEST_DEPLOY_STATUS = host + port2 + version + '/deployment/deploy-status/{deployJobName}'

export const GET_FAIL_DEPLOYMENT = host + version + '/ci-deployments/stats/failures?limit=10&pipeline={0}&name={1}'

export const GET_VERIFY_LIST = 'http://10.220.132.200:8081' + version + '/testbed-records?page={0}'

export const POST_VERIFY_LIST = 'http://10.220.132.200:8081' + version + '/testbed-records'

export const GET_TESTBED_INFO = 'http://172.29.42.59:8080/api/0.1/appliances/search/legacy-dashboard?limit=10&name={0}'

export const CANCEL_CHECK_APPLIANCE = 'http://10.220.132.200:8081' + version + '/testbed/cancel?hostname={0}'
