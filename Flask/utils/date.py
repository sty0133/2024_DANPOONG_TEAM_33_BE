from datetime import datetime

def add_d_day_to_companies(company_list):
    """
    회사 리스트에 D-day 값을 추가.

    Args:
        company_list (list): 회사 데이터가 포함된 리스트.

    Returns:
        list: D-day 값이 추가된 회사 데이터 리스트.
    """
    # 포멧 변경 후 D-day 계산
    company_list = serialize_company_list(company_list)

    for company in company_list:
        end_date = company['hiringPeriodEndDate']
        if end_date:
            company["hiringPeriodDday"] = calculate_d_day(end_date)
    return company_list

def calculate_d_day(date_str):
    """
    주어진 날짜 문자열을 날짜 객체로 변환하고 오늘을 기준으로 D-day 계산.
    
    Args:
        date_str (str): "YYYY-MM-DD" 형식의 날짜 문자열.

    Returns:
        int: 오늘 기준으로 몇 일 뒤인지 (양수: 미래, 음수: 과거, 0: 오늘).
    """
    target_date = datetime.strptime(date_str, "%Y-%m-%dT%H:%M:%S.%f").date()
    today = datetime.now().date()
    d_day = (target_date - today).days
    return d_day

def serialize_company_list(company_list):

    for company in company_list:
        # 날짜를 문자열로 변환
        if 'hiringPeriodEndDate' in company:
            company['hiringPeriodEndDate'] = company['hiringPeriodEndDate'].isoformat()

        elif 'hiringPeriodStartDate' in company:
            company['hiringPeriodStartDate'] = company['hiringPeriodStartDate'].isoformat()

    return company_list

