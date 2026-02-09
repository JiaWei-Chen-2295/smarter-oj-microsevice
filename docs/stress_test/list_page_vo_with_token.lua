wrk.method = "POST"
wrk.body   = '{"current":1,"pageSize":20}'
wrk.headers["Content-Type"] = "application/json"

local token = os.getenv("SATOKEN") or "your-satoken-here"
wrk.headers["Cookie"] = "satoken=" .. token
