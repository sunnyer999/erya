import moment from 'moment'
import 'moment-duration-format'

export function truncate (jobName) {
  if (jobName.startsWith('nba_main_regression')) {
    return jobName.slice(20)
  }
  if (jobName.startsWith('nba_main_long_regression')) {
    return jobName.slice(25)
  }
  if (jobName.startsWith('nba_upgrade')) {
    return jobName.slice(12)
  }
  if (jobName.startsWith('velocity_main_regression_')) {
    return jobName.slice(25)
  }

  return jobName
}

export function date (time, format) {
  return moment(time).format(format)
}

export function duration (timespan, format, trim) {
  return moment.duration(timespan).format(format, {trim: trim})
}
